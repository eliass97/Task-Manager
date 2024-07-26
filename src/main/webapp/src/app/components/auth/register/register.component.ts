import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { RegistrationRequest } from '../../../models/auth';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  public registerForm!: FormGroup;

  constructor(
    private router: Router,
    private toastrService: ToastrService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }

  public register(): void {
    const registrationRequest = new RegistrationRequest(
      this.registerForm.get('email')?.value,
      this.registerForm.get('username')?.value,
      this.registerForm.get('password')?.value
    );
    this.userService.registerUser(registrationRequest).subscribe({
      next: () => {
        this.toastrService.success('You have successfully registered. Please check your email for the account activation link.');
        this.router.navigate(['/login']);
      },
      error: (error: HttpErrorResponse) => {
        this.toastrService.error(JSON.parse(error.error)['message'], 'Failed to register');
      }
    });
  }
}
