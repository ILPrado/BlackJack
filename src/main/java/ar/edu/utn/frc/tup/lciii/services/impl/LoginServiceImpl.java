package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.player.PlayerResponseDTO;
import ar.edu.utn.frc.tup.lciii.models.Player;
import ar.edu.utn.frc.tup.lciii.services.LoginService;
import ar.edu.utn.frc.tup.lciii.services.PlayerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Player login(String userName, String password) {
        return playerService.getPlayerByUserNameAndPassword(userName,password);
    }
}
