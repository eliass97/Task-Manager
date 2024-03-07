import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { LoginRequest } from "../../../model/auth";
import { AuthenticationService } from "../../../service/authentication/authentication.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.css"]
})
export class LoginComponent implements OnInit {

  public loginForm!: FormGroup;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
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
    this.authenticationService.login(loginRequest).subscribe(
      () => this.router.navigate(['/home']),
      (error) => console.log(error)
    )
  }
}
