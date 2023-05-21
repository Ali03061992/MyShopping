import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ObjectContainingImageFormService } from './object-containing-image-form.service';
import { ObjectContainingImageService } from '../service/object-containing-image.service';
import { IObjectContainingImage } from '../object-containing-image.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ObjectContainingImageUpdateComponent } from './object-containing-image-update.component';

describe('ObjectContainingImage Management Update Component', () => {
  let comp: ObjectContainingImageUpdateComponent;
  let fixture: ComponentFixture<ObjectContainingImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let objectContainingImageFormService: ObjectContainingImageFormService;
  let objectContainingImageService: ObjectContainingImageService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ObjectContainingImageUpdateComponent],
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
      .overrideTemplate(ObjectContainingImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjectContainingImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    objectContainingImageFormService = TestBed.inject(ObjectContainingImageFormService);
    objectContainingImageService = TestBed.inject(ObjectContainingImageService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const objectContainingImage: IObjectContainingImage = { id: 456 };
      const json: IProduct = { id: 15848 };
      objectContainingImage.json = json;

      const productCollection: IProduct[] = [{ id: 70426 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [json];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ objectContainingImage });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const objectContainingImage: IObjectContainingImage = { id: 456 };
      const json: IProduct = { id: 1976 };
      objectContainingImage.json = json;

      activatedRoute.data = of({ objectContainingImage });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(json);
      expect(comp.objectContainingImage).toEqual(objectContainingImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjectContainingImage>>();
      const objectContainingImage = { id: 123 };
      jest.spyOn(objectContainingImageFormService, 'getObjectContainingImage').mockReturnValue(objectContainingImage);
      jest.spyOn(objectContainingImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectContainingImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objectContainingImage }));
      saveSubject.complete();

      // THEN
      expect(objectContainingImageFormService.getObjectContainingImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(objectContainingImageService.update).toHaveBeenCalledWith(expect.objectContaining(objectContainingImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjectContainingImage>>();
      const objectContainingImage = { id: 123 };
      jest.spyOn(objectContainingImageFormService, 'getObjectContainingImage').mockReturnValue({ id: null });
      jest.spyOn(objectContainingImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectContainingImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: objectContainingImage }));
      saveSubject.complete();

      // THEN
      expect(objectContainingImageFormService.getObjectContainingImage).toHaveBeenCalled();
      expect(objectContainingImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IObjectContainingImage>>();
      const objectContainingImage = { id: 123 };
      jest.spyOn(objectContainingImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ objectContainingImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(objectContainingImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProduct', () => {
      it('Should forward to productService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(productService, 'compareProduct');
        comp.compareProduct(entity, entity2);
        expect(productService.compareProduct).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
