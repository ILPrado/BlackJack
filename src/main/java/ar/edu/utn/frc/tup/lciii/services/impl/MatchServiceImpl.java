package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.match.MatchResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.match.NewMatchRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.play.PlayRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.play.PlayResponseDTO;
import ar.edu.utn.frc.tup.lciii.dtos.player.NewPlayerRequestDTO;
import ar.edu.utn.frc.tup.lciii.entities.MatchEntity;
import ar.edu.utn.frc.tup.lciii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.MatchJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.DeckService;
import ar.edu.utn.frc.tup.lciii.services.MatchService;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Autowired
    private PlayerService playerService;
    @Autowired
    private DeckService deckService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MatchResponseDTO> getMatchesByPlayer(Long playerId) {
        List<MatchResponseDTO> matches = new ArrayList<>();
        Optional<List<MatchEntity>> listOptional = matchJpaRepository.getAllByPlayer(playerId);
        if(listOptional.isPresent()){
            for (MatchEntity match: listOptional.get()) {
                Integer score = 0;
                for (Card c: match.getPlayerHand()){
                    score = score+c.getValue().intValue();
                }
                MatchResponseDTO matchDTO = modelMapper.map(match,MatchResponseDTO.class);
                matchDTO.setPlayerScore(score);
                matches.add(matchDTO);

            }
        }
        return matches;
    }

    @Override
    public MatchResponseDTO createMatch(NewMatchRequestDTO newMatchRequestDTO) {
        Player player = playerService.getPlayerById(newMatchRequestDTO.getPlayerId());
        playerService.savePlayer(new NewPlayerRequestDTO("App","",""));
        Player app = playerService.getPlayerLastId();

        if(player.equals(null)){
            throw  new EntityNotFoundException("The user "+player.getId()+" do not exist");
        } else if (app.equals(null)) {
            throw  new EntityNotFoundException("The user "+app.getId()+" do not exist");
        }else {
            MatchEntity matchEntity = new MatchEntity();
            matchEntity.setDeck(deckService.createDeck());
            deckService.shuffleDeck(matchEntity.getDeck());
            matchEntity.setNextCardIndex(0);
            matchEntity.setPlayerHand(new ArrayList<>());
            matchEntity.setAppHand(new ArrayList<>());
            takeCards(matchEntity);
            matchEntity.setBetPlayer(newMatchRequestDTO.getBetPlayer().intValue());
            player.setBalance(BigDecimal.valueOf(player.getBalance().intValue()-newMatchRequestDTO.getBetPlayer().intValue()));
            matchEntity.setPlayer(modelMapper.map(player, PlayerEntity.class));
            matchEntity.setAppPlayer(modelMapper.map(app, PlayerEntity.class));
            matchEntity.setWinner(null);
            matchEntity.setMatchStatus(Status.PLAYING);
            matchJpaRepository.save(matchEntity);
            MatchResponseDTO matchResponseDTO =  modelMapper.map(matchEntity,MatchResponseDTO.class);
            matchResponseDTO.setPlayerScore(0);
            for (Card c: matchEntity.getPlayerHand()) {
                matchResponseDTO.setPlayerScore(matchResponseDTO.getPlayerScore()+c.getValue().intValue());

            }
            matchResponseDTO.setPlayerHand(matchEntity.getPlayerHand().toString());
            matchResponseDTO.setAppFirstCard(matchEntity.getAppHand().get(0).toString());

            return matchResponseDTO;
        }

    }

    private void takeCards(MatchEntity matchEntity) {
        for (int i = 0; i<2;++i){
            matchEntity.getPlayerHand().add(deckService.takeCard(matchEntity.getDeck(),matchEntity.getNextCardIndex()));
            matchEntity.setNextCardIndex(matchEntity.getNextCardIndex()+1);
            matchEntity.getAppHand().add(deckService.takeCard(matchEntity.getDeck(),matchEntity.getNextCardIndex()));
            matchEntity.setNextCardIndex(matchEntity.getNextCardIndex()+1);
        }
    }

    @Override
    public Match getMatchById(Long id) {
        MatchEntity me = matchJpaRepository.getReferenceById(id);
        if(me != null) {
            Match match = modelMapper.map(me, Match.class);
            return match;
        }else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public MatchResponseDTO getMatchResponseDTOById(Long id) {
        MatchEntity me = matchJpaRepository.getReferenceById(id);
        if(me != null) {
            return modelMapper.map(me, MatchResponseDTO.class);
        }else {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    @Override
    public PlayResponseDTO play(Long matchId, PlayRequestDTO play) {
        PlayResponseDTO playResponseDTO = new PlayResponseDTO();
        playResponseDTO.setMatchStatus(Status.PLAYING);
        playResponseDTO.setWinner(null);

        Match match = this.getMatchById(matchId);
        Player player = playerService.getPlayerById(play.getPlayer());
        Integer playerScore = 0;
        Integer appScore = 0;

        if(match==null){
            throw new EntityNotFoundException();
        }
        if (player==null){
            throw new EntityNotFoundException();
        }
        if(play.getDecision().equals(PlayDecision.TAKE)){
            match.getPlayerHand().add(deckService.takeCard(match.getDeck(),match.getNextCardIndex()));
            match.setNextCardIndex(match.getNextCardIndex()+1);
            playResponseDTO.setPlayerHand(match.getPlayerHand().toString());
            playerScore=0;
            for (Card c: match.getPlayerHand()) {
                playerScore=playerScore+c.getValue().intValue();
            }
            playResponseDTO.setPlayerScore(playerScore);
            playResponseDTO.setAppHand(match.getAppHand().get(0).toString());
        }else if(play.getDecision().equals(PlayDecision.STOP)){
            do{
                playResponseDTO.setPlayerHand(match.getPlayerHand().toString());
                playerScore=0;
                for (Card c: match.getPlayerHand()) {
                    playerScore=playerScore+c.getValue().intValue();
                }
                playResponseDTO.setPlayerScore(playerScore);
                match.getAppHand().add(deckService.takeCard(match.getDeck(),match.getNextCardIndex()));
                match.setNextCardIndex(match.getNextCardIndex()+1);
                appScore=0;
                for (Card c: match.getAppHand()) {
                    appScore=appScore+c.getValue().intValue();
                }
                playResponseDTO.setAppHand(match.getAppHand().toString());
                playResponseDTO.setAppScore(appScore);
            }while (appScore<16);
            playResponseDTO.setAppHand(match.getAppHand().toString());
            if(playerScore>=appScore||appScore>21){

                playResponseDTO.setMatchStatus(Status.FINISH);
                playResponseDTO.setWinner(match.getPlayer().getUserName());
                playerService.updatePlayerBalance(match.getPlayer(), BigDecimal.valueOf(match.getPlayer().getBalance().intValue()+(match.getBetPlayer()*1.5)));
                match.setWinner(match.getPlayer());
            }else {
                playResponseDTO.setMatchStatus(Status.FINISH);
                playResponseDTO.setWinner(match.getAppPlayer().getUserName());
            }   match.setWinner(match.getAppPlayer());

        }else {
            throw new EntityNotFoundException("Valor Incorrecto!!!");
        }
        if(playerScore>21){
            playResponseDTO.setMatchStatus(Status.FINISH);
            playResponseDTO.setWinner(match.getAppPlayer().getUserName());
            appScore=0;
            for (Card c: match.getAppHand()) {
                appScore=appScore+c.getValue().intValue();
            }
            playResponseDTO.setAppHand(match.getAppHand().toString());
            playResponseDTO.setAppScore(appScore);
        }

        playResponseDTO.setPlayer(play.getPlayer());
        playResponseDTO.setDecision(play.getDecision());
        playResponseDTO.setBetPlayer(match.getBetPlayer());
        playResponseDTO.setCardsInDeck(match.getDeck().getCards().size()-match.getNextCardIndex());
        matchJpaRepository.save(modelMapper.map(match,MatchEntity.class));
        return playResponseDTO;
    }

    private Integer compareCards(Card card1, Card card2) {
        return card1.getValue().compareTo(card2.getValue());
    }
}
