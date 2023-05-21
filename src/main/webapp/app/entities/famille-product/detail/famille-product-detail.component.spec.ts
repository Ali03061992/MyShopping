import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilleProductDetailComponent } from './famille-product-detail.component';

describe('FamilleProduct Management Detail Component', () => {
  let comp: FamilleProductDetailComponent;
  let fixture: ComponentFixture<FamilleProductDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FamilleProductDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ familleProduct: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FamilleProductDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FamilleProductDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load familleProduct on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.familleProduct).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
