import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './auth-layout.html',
  styleUrl: './auth-layout.css'
})
export class AuthLayoutComponent {
  @Input() title = '';
  @Input() subtitle = '';
  @Input() icon = '';
  @Input() iconRotation: 'rotate-6' | '-rotate-6' | 'rotate-0' = 'rotate-0';
  @Input() cardMaxWidth: 'max-w-md' | 'max-w-lg' = 'max-w-md';
}