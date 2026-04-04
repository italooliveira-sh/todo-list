import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  // Verifica se o token existe no localStorage
  if (tokenService.getToken()) {
    return true;
  }

  // Se não existir, redireciona para o login e bloqueia a rota
  router.navigate(['/login']);
  return false;
};