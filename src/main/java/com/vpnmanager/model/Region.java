package com.vpnmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "regions")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String command;

    public Region() {}

    public Region(String name, String command) {
        this.name = name;
        this.command = command;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCommand() { return command; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCommand(String command) { this.command = command; }
}
