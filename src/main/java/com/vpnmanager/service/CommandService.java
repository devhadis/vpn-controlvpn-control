package com.vpnmanager.service;

import org.springframework.stereotype.Service;

@Service
public class CommandService {

    public String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return "Comando executado com sucesso.";
        } catch (Exception e) {
            return "Erro ao executar o comando: " + e.getMessage();
        }
    }
}

