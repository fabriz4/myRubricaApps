import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GwRubricaTestModule } from '../../../../test.module';
import { ContattiDetailComponent } from 'app/entities/serviziorubrica/contatti/contatti-detail.component';
import { Contatti } from 'app/shared/model/serviziorubrica/contatti.model';

describe('Component Tests', () => {
  describe('Contatti Management Detail Component', () => {
    let comp: ContattiDetailComponent;
    let fixture: ComponentFixture<ContattiDetailComponent>;
    const route = ({ data: of({ contatti: new Contatti(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GwRubricaTestModule],
        declarations: [ContattiDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContattiDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContattiDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contatti).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
