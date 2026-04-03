import { Component, inject, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { TokenService } from '../../../../core/services/token';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class HeaderComponent {
  private tokenService = inject(TokenService);
  private router = inject(Router);

  @Output() toggleMenu = new EventEmitter<void>();

  userName = 'Italo Oliveira';
  userInitials = 'IO';

  logout(): void {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  onToggleMenu(): void {
    this.toggleMenu.emit();
  }
}
