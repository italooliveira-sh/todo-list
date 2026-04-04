import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token';

export const guestGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  // Se o token existe, o usuário já está logado
  if (tokenService.getToken()) {
    // Redireciona para o dashboard e impede o acesso ao login/register
    router.navigate(['/dashboard']);
    return false;
  }

  // Se não existir token, permite o acesso às telas de login/register
  return true;
};