import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GwRubricaSharedModule } from 'app/shared/shared.module';
import { ContattiComponent } from './contatti.component';
import { ContattiDetailComponent } from './contatti-detail.component';
import { ContattiUpdateComponent } from './contatti-update.component';
import { ContattiDeleteDialogComponent } from './contatti-delete-dialog.component';
import { contattiRoute } from './contatti.route';
import { FormsModule } from '@angular/forms';

// search module
import { Ng2SearchPipeModule } from 'ng2-search-filter';

@NgModule({
  imports: [GwRubricaSharedModule, RouterModule.forChild(contattiRoute), FormsModule, Ng2SearchPipeModule ],
  declarations: [ContattiComponent, ContattiDetailComponent, ContattiUpdateComponent, ContattiDeleteDialogComponent],
  entryComponents: [ContattiDeleteDialogComponent]
})
export class ServiziorubricaContattiModule {}
