import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth';
import { AuthLayoutComponent } from '../../../shared/components/layout/auth-layout/auth-layout';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatSnackBarModule,
    AuthLayoutComponent
  ],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  private formBuilder = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  // Definindo o formulário de registro com validações
  registerForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]]
  }, { validators: this.passwordMatchValidator }); // Adicionamos um validador customizado para comparar as senhas

  hidePassword = true;
  hideConfirmPassword = true;

  // Função para validar se a senha e a confirmação são iguais
  private passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const { name, email, password } = this.registerForm.getRawValue();
      
      this.authService.register({ name: name!, email: email!, password: password! }).subscribe({
        next: () => {
          this.snackBar.open('Conta criada com sucesso!', 'Fechar', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
          });
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error('Erro ao realizar registro', err);
          
          let errorMessage = err.error?.message || 'Ocorreu um erro ao criar sua conta.';
          
          // Se houver erros detalhados por campo, pega o primeiro para exibir
          if (err.error?.errors) {
            const fieldErrors = Object.values(err.error.errors);
            if (fieldErrors.length > 0) {
              errorMessage = fieldErrors[0] as string;
            }
          }
          
          this.snackBar.open(errorMessage, 'Fechar', {
            duration: 5000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }
}