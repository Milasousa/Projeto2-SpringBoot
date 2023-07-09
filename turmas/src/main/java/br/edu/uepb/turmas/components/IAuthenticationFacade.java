package br.edu.uepb.turmas.components;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthenticationInfo();
}