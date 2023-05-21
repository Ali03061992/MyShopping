import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FamilleProductFormService } from './famille-product-form.service';
import { FamilleProductService } from '../service/famille-product.service';
import { IFamilleProduct } from '../famille-product.model';

import { FamilleProductUpdateComponent } from './famille-product-update.component';

describe('FamilleProduct Management Update Component', () => {
  let comp: FamilleProductUpdateComponent;
  let fixture: ComponentFixture<FamilleProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familleProductFormService: FamilleProductFormService;
  let familleProductService: FamilleProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FamilleProductUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FamilleProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familleProductFormService = TestBed.inject(FamilleProductFormService);
    familleProductService = TestBed.inject(FamilleProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const familleProduct: IFamilleProduct = { id: 456 };

      activatedRoute.data = of({ familleProduct });
      comp.ngOnInit();

      expect(comp.familleProduct).toEqual(familleProduct);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilleProduct>>();
      const familleProduct = { id: 123 };
      jest.spyOn(familleProductFormService, 'getFamilleProduct').mockReturnValue(familleProduct);
      jest.spyOn(familleProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familleProduct }));
      saveSubject.complete();

      // THEN
      expect(familleProductFormService.getFamilleProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(familleProductService.update).toHaveBeenCalledWith(expect.objectContaining(familleProduct));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilleProduct>>();
      const familleProduct = { id: 123 };
      jest.spyOn(familleProductFormService, 'getFamilleProduct').mockReturnValue({ id: null });
      jest.spyOn(familleProductService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleProduct: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familleProduct }));
      saveSubject.complete();

      // THEN
      expect(familleProductFormService.getFamilleProduct).toHaveBeenCalled();
      expect(familleProductService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilleProduct>>();
      const familleProduct = { id: 123 };
      jest.spyOn(familleProductService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familleProduct });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familleProductService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
