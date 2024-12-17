package com.vpnmanager.controller;

import com.vpnmanager.service.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vpn")
public class VpnController {

    @Autowired
    private CommandService commandService;

    @PostMapping("/start/{command}")
    public String startVpn(@PathVariable String command) {
        return commandService.executeCommand("sudo systemctl start " + command);
    }

    @PostMapping("/stop")
    public String stopVpn() {
        return commandService.executeCommand("sudo systemctl stop openvpn@server");
    }
}
