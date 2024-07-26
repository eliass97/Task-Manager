import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [],
  templateUrl: './confirm-modal.component.html',
  styleUrl: './confirm-modal.component.css'
})
export class ConfirmModalComponent {

  @Input() public title!: string;
  @Input() public message!: string;

  constructor(private modal: NgbActiveModal) { }

  public close(value: boolean) {
    this.modal.close(value);
  }
}
