import { Component } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContatti } from 'app/shared/model/serviziorubrica/contatti.model';
import { ContattiService } from './contatti.service';

@Component({
  templateUrl: './contatti-delete-dialog.component.html'
})
export class ContattiDeleteDialogComponent {
  contatti: IContatti;

  constructor(protected contattiService: ContattiService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.contattiService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'contattiListModification',
        content: 'Deleted an contatti'
      });
      this.activeModal.dismiss(true);
    });
  }
}
