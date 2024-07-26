import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginRequest } from '../../../models/auth';
import { AuthenticationService } from '../../../services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public loginForm!: FormGroup;

  constructor(
    private router: Router,
    private toastrService: ToastrService,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }

  public login(): void {
    const loginRequest = new LoginRequest(
      this.loginForm.get('username')?.value,
      this.loginForm.get('password')?.value
    );
    this.authenticationService.login(loginRequest).subscribe({
      next: () => {
        this.toastrService.success('You have successfully logged in');
        this.router.navigate(['/home']);
      },
      error: () => {
        this.toastrService.error('Wrong username or password');
      }
    });
  }
}
