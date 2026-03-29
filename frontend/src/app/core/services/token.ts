import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly = 'auth-token';

  saveToken(token: string): void {
    localStorage.setItem(this.readonly, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.readonly);
  }

  removeToken(): void {
    localStorage.removeItem(this.readonly);
  }
}
