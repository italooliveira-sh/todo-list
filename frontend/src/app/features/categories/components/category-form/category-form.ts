import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CategoryService } from '../../../../core/services/category';
import { Category } from '../../../../shared/models/task.model';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule
  ],
  templateUrl: './category-form.html',
  styleUrl: './category-form.css'
})
export class CategoryFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private categoryService = inject(CategoryService);
  public dialogRef = inject(MatDialogRef<CategoryFormComponent>);
  private snackBar = inject(MatSnackBar);
  public data = inject<Category | undefined>(MAT_DIALOG_DATA);

  form!: FormGroup;
  isEdit = false;

  ngOnInit(): void {
    this.isEdit = !!this.data;
    this.initForm();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name: [this.data?.name || '', [Validators.required, Validators.maxLength(50)]],
      color: [this.data?.color || '#3b82f6', [Validators.required, Validators.pattern(/^#[0-9A-Fa-f]{6}$/)]]
    });
  }

  updateColor(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.form.get('color')?.setValue(input.value);
  }

  onSubmit(): void {

    if (this.form.invalid) return;

    const request = this.form.value;
    
    if (this.isEdit && this.data) {
      this.categoryService.update(this.data.id, request).subscribe({
        next: () => {
          this.snackBar.open('Categoria atualizada!', 'Sucesso', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => this.snackBar.open('Erro ao atualizar categoria', 'Erro', { duration: 3000 })
      });
    } else {
      this.categoryService.create(request).subscribe({
        next: () => {
          this.snackBar.open('Categoria criada!', 'Sucesso', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => this.snackBar.open('Erro ao criar categoria', 'Erro', { duration: 3000 })
      });
    }
  }
}
