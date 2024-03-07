import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../service/user/user.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RegistrationRequest } from '../../../model/auth';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  public registerForm!: FormGroup;

  constructor(
    private router: Router,
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
    this.userService.registerUser(registrationRequest).subscribe(
      () => this.router.navigate(['/login']),
      (error: any) => console.log(error)
    )
  }
}
