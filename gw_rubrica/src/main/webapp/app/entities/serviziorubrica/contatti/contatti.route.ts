import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Contatti } from 'app/shared/model/serviziorubrica/contatti.model';
import { ContattiService } from './contatti.service';
import { ContattiComponent } from './contatti.component';
import { ContattiDetailComponent } from './contatti-detail.component';
import { ContattiUpdateComponent } from './contatti-update.component';
import { IContatti } from 'app/shared/model/serviziorubrica/contatti.model';

@Injectable({ providedIn: 'root' })
export class ContattiResolve implements Resolve<IContatti> {
  constructor(private service: ContattiService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContatti> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((contatti: HttpResponse<Contatti>) => contatti.body));
    }
    return of(new Contatti());
  }
}

export const contattiRoute: Routes = [
  {
    path: '',
    component: ContattiComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contattis'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContattiDetailComponent,
    resolve: {
      contatti: ContattiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contattis'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContattiUpdateComponent,
    resolve: {
      contatti: ContattiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contattis'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContattiUpdateComponent,
    resolve: {
      contatti: ContattiResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Contattis'
    },
    canActivate: [UserRouteAccessService]
  }
];
