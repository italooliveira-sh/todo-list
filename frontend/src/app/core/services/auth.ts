import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { TokenService } from './token';
import { LoginRequest, LoginResponse, RegisterRequest, UserResponse } from '../../shared/models/auth.model';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private tokenService = inject(TokenService);
  private readonly API_HOST = 'http://localhost:8080/api';

  login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_HOST}/auth/login`, data).pipe(
      tap(res => {
        this.tokenService.saveToken(res.token);
      })
    );
  }

  register(data: RegisterRequest): Observable<UserResponse> {
    return this.http.post<UserResponse>(`${this.API_HOST}/users`, data);
  }

}