import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'contatti',
        loadChildren: () => import('./serviziorubrica/contatti/contatti.module').then(m => m.ServiziorubricaContattiModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class GwRubricaEntityModule {}
