import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DEFAULT_MODAL_OPTIONS } from '../constants/constants';
import { ProfileModalComponent } from '../modals/profile/profile-modal.component';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(
    private modalService: NgbModal,
    private authenticationService: AuthenticationService
  ) { }

  public isLoggedIn(): boolean {
    return this.authenticationService.isLoggedIn();
  }

  public viewProfile(): void {
    const userId = this.authenticationService.getUserId();
    if (userId) {
      const modalRef = this.modalService.open(ProfileModalComponent, DEFAULT_MODAL_OPTIONS);
      modalRef.componentInstance.userId = userId;
    }
  }

  public logout(): void {
    this.authenticationService.logout();
  }
}
