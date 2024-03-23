package me.dhassan.api.contexts;


import jakarta.enterprise.context.RequestScoped;

import java.util.UUID;

@RequestScoped
public class SecurityContext {
    public UUID userId;
}
