import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IContatti, Contatti } from 'app/shared/model/serviziorubrica/contatti.model';
import { ContattiService } from './contatti.service';

@Component({
  selector: 'jhi-contatti-update',
  templateUrl: './contatti-update.component.html'
})
export class ContattiUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    surname: [],
    phone: [],
    email: [],
    owner: []
  });

  constructor(protected contattiService: ContattiService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ contatti }) => {
      this.updateForm(contatti);
    });
  }

  updateForm(contatti: IContatti) {
    this.editForm.patchValue({
      id: contatti.id,
      name: contatti.name,
      surname: contatti.surname,
      phone: contatti.phone,
      email: contatti.email,
      owner: contatti.owner
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const contatti = this.createFromForm();
    if (contatti.id !== undefined) {
      this.subscribeToSaveResponse(this.contattiService.update(contatti));
    } else {
      this.subscribeToSaveResponse(this.contattiService.create(contatti));
    }
  }

  private createFromForm(): IContatti {
    return {
      ...new Contatti(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      surname: this.editForm.get(['surname']).value,
      phone: this.editForm.get(['phone']).value,
      email: this.editForm.get(['email']).value,
      owner: this.editForm.get(['owner']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContatti>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
