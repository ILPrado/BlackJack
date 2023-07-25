package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.player.NewPlayerRequestDTO;
import ar.edu.utn.frc.tup.lciii.dtos.player.PlayerResponseDTO;
import ar.edu.utn.frc.tup.lciii.entities.PlayerEntity;
import ar.edu.utn.frc.tup.lciii.models.Player;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.PlayerJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerJpaRepository playerJpaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Player getPlayerById(Long id) {
        PlayerEntity playerEntity = playerJpaRepository.getReferenceById(id);
        if(Objects.isNull(playerEntity.getUserName())) {
            throw new EntityNotFoundException();
        }
        return modelMapper.map(playerEntity, Player.class);
    }

    @Override
    public PlayerResponseDTO getPlayerResponseDTOById(Long id) {
        PlayerEntity playerEntity = playerJpaRepository.getReferenceById(id);
        if(Objects.isNull(playerEntity.getUserName())) {
            throw new EntityNotFoundException();
        }
        return modelMapper.map(playerEntity, PlayerResponseDTO.class);
    }

    @Override
    public Player updatePlayerBalance(Player player, BigDecimal newBalance) {
        PlayerEntity playerEntity = playerJpaRepository.getReferenceById(player.getId());
        playerEntity.setBalance(newBalance);
        return modelMapper.map(playerJpaRepository.save(playerEntity),Player.class);
    }

    @Override
    public Player getPlayerByUserNameAndPassword(String userName, String password) {
        Optional<PlayerEntity> player = playerJpaRepository.findByUserNameAndPassword(userName, password);
        if(player.isPresent()){
            return modelMapper.map(player,Player.class);
        }else {
            throw new EntityNotFoundException ("Username or password invalid!");
        }
    }

    @Override
    public PlayerResponseDTO savePlayer(NewPlayerRequestDTO newPlayerRequestDTO) {
        PlayerEntity playerEntity = modelMapper.map(newPlayerRequestDTO,PlayerEntity.class);
        playerEntity.setBalance(BigDecimal.valueOf(200));
        return modelMapper.map(playerJpaRepository.save(playerEntity), PlayerResponseDTO.class);
    }

    @Override
    public Player getPlayerLastId() {
        PlayerEntity playerEntity = playerJpaRepository.getReferenceLastId();
        if(Objects.isNull(playerEntity.getUserName())) {
            throw new EntityNotFoundException();
        }
        return modelMapper.map(playerEntity, Player.class);
    }

}
