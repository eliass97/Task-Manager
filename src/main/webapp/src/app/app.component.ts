import { Component } from "@angular/core";
import { AuthenticationService } from "../service/authentication/authentication.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  public logout() {
    this.authenticationService.logout();
  }
}
