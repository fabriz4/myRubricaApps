import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContatti } from 'app/shared/model/serviziorubrica/contatti.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ContattiService } from './contatti.service';
import { ContattiDeleteDialogComponent } from './contatti-delete-dialog.component';

@Component({
  selector: 'jhi-contatti',
  templateUrl: './contatti.component.html'
})
export class ContattiComponent implements OnInit, OnDestroy {
  contattis: IContatti[];
  eventSubscriber: Subscription;
  itemsPerPage: number;
  links: any;
  page: any;
  predicate: any;
  reverse: any;
  totalItems: number;

  constructor(
    protected contattiService: ContattiService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.contattis = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.reverse = true;
  }

  loadAll() {
    this.contattiService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IContatti[]>) => this.paginateContattis(res.body, res.headers));
  }

  reset() {
    this.page = 0;
    this.contattis = [];
    this.loadAll();
  }

  loadPage(page) {
    this.page = page;
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInContattis();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IContatti) {
    return item.id;
  }

  registerChangeInContattis() {
    this.eventSubscriber = this.eventManager.subscribe('contattiListModification', () => this.reset());
  }

  delete(contatti: IContatti) {
    const modalRef = this.modalService.open(ContattiDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contatti = contatti;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateContattis(data: IContatti[], headers: HttpHeaders) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
    for (let i = 0; i < data.length; i++) {
      this.contattis.push(data[i]);
    }
  }
}
