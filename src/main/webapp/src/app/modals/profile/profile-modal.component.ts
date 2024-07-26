import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { DEFAULT_MODAL_OPTIONS } from '../../constants/constants';
import { JobTitle } from '../../models/type';
import { UserData } from '../../models/user';
import { JobTitleService } from '../../services/job-title.service';
import { UserService } from '../../services/user.service';
import { ConfirmModalComponent } from '../confirm/confirm-modal.component';

@Component({
  selector: 'app-profile-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './profile-modal.component.html',
  styleUrl: './profile-modal.component.css'
})
export class ProfileModalComponent implements OnInit {

  @Input() public userId!: number;

  private userData: UserData = {};

  public userImageUrl: string | SafeUrl = '../assets/images/no_profile_pic.png';
  public jobTitles: JobTitle[] = [];
  public profileForm = new FormGroup({
    firstName: new FormControl(),
    lastName: new FormControl(),
    email: new FormControl(),
    location: new FormControl(),
    phone: new FormControl(),
    jobTitle: new FormControl(),
  });

  constructor(
    private modal: NgbActiveModal,
    private modalService: NgbModal,
    private sanitizer: DomSanitizer,
    private toastrService: ToastrService,
    private userService: UserService,
    private jobTitleService: JobTitleService
  ) { }

  ngOnInit(): void {
    this.profileForm.disable();
    this.getJobTitles();
    this.getUserData();
  }

  private getJobTitles(): void {
    this.jobTitleService.getAllTitles().subscribe({
      next: resp => this.jobTitles = resp,
      error: (error: HttpErrorResponse) => this.toastrService.error(error.error.message)
    });
  }

  private getUserData(): void {
    this.userService.getUserData(this.userId).subscribe({
      next: resp => this.patchUserData(resp),
      error: (error: HttpErrorResponse) => this.toastrService.error(error.error.message)
    });
  }

  private patchUserData(userData: UserData): void {
    this.userData = userData;

    this.profileForm.patchValue({
      firstName: userData.firstName,
      lastName: userData.lastName,
      email: userData.email,
      location: userData.location,
      phone: userData.phone,
      jobTitle: userData.jobTitle
    });

    if (userData.image) {
      const url = 'data:image/jpeg;base64,' + userData.image;
      this.userImageUrl = this.sanitizer.bypassSecurityTrustUrl(url);
    }
  }

  public close(): void {
    this.modal.close();
  }

  public onEdit(): void {
    this.profileForm.enable();
    this.profileForm.get('email')?.disable();
  }

  public onCancel(): void {
    this.profileForm.disable();
    this.patchUserData(this.userData);
  }

  public onSave(): void {
    const updatedUserData = { ...this.userData };
    updatedUserData.firstName = this.profileForm.get('firstName')?.value;
    updatedUserData.lastName = this.profileForm.get('lastName')?.value;
    updatedUserData.location = this.profileForm.get('location')?.value;
    updatedUserData.phone = this.profileForm.get('phone')?.value;
    updatedUserData.jobTitle = this.profileForm.get('jobTitle')?.value;

    this.userService.updateUserProfile(updatedUserData).subscribe({
      next: response => {
        this.patchUserData(response);
        this.profileForm.disable();
        this.toastrService.success('Your profile has been updated');
      },
      error: (error: HttpErrorResponse) => this.toastrService.error(error.error.message)
    });
  }

  public onImageInputClick(event: Event): void {
    (event.target as HTMLInputElement).value = '';
  }

  public onImageInputChange(event: Event): void {
    const file = (event.target as HTMLInputElement)?.files?.item(0);
    if (!file) return;

    const modalRef = this.modalService.open(ConfirmModalComponent, DEFAULT_MODAL_OPTIONS);
    modalRef.componentInstance.title = "Confirm image change";
    modalRef.componentInstance.message = "Are you sure you want to upload the new profile image?";
    modalRef.result.then(
      answer => {
        if (!answer) return;

        this.userService.updateUserImage(this.userId, file).subscribe({
          next: (response: string) => {
            this.toastrService.success(response);
            this.getUserData();
          },
          error: (error: HttpErrorResponse) => this.toastrService.error(error.error.message)
        });
      }
    );
  }
}
