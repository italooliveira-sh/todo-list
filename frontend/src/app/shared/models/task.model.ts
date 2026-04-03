export enum TaskStatus {
  PENDING = 'PENDING',
  DOING = 'DOING',
  DONE = 'DONE'
}

export enum Priority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH'
}

export interface Category {
  id: string;
  name: string;
  color?: string;
  taskCount?: number;
}

export interface Task {
  id: string;
  title: string;
  description: string;
  status: TaskStatus;
  statusDescription: string;
  priority: Priority;
  priorityDescription: string;
  deadline?: string;
  category: Category;
  createdAt: string;
  updatedAt: string;
}

export interface TaskRequest {
  title: string;
  description: string;
  status?: TaskStatus;
  priority: Priority;
  categoryId: string;
  deadline?: string;
}

export interface CategoryRequest {
  name: string;
  color: string;
}