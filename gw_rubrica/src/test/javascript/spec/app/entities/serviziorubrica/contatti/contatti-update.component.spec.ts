import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GwRubricaTestModule } from '../../../../test.module';
import { ContattiUpdateComponent } from 'app/entities/serviziorubrica/contatti/contatti-update.component';
import { ContattiService } from 'app/entities/serviziorubrica/contatti/contatti.service';
import { Contatti } from 'app/shared/model/serviziorubrica/contatti.model';

describe('Component Tests', () => {
  describe('Contatti Management Update Component', () => {
    let comp: ContattiUpdateComponent;
    let fixture: ComponentFixture<ContattiUpdateComponent>;
    let service: ContattiService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GwRubricaTestModule],
        declarations: [ContattiUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContattiUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContattiUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContattiService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contatti(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contatti();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
