import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContatti } from 'app/shared/model/serviziorubrica/contatti.model';

@Component({
  selector: 'jhi-contatti-detail',
  templateUrl: './contatti-detail.component.html'
})
export class ContattiDetailComponent implements OnInit {
  contatti: IContatti;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contatti }) => {
      this.contatti = contatti;
    });
  }

  previousState() {
    window.history.back();
  }
}
