import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImagePresentationFormService } from './image-presentation-form.service';
import { ImagePresentationService } from '../service/image-presentation.service';
import { IImagePresentation } from '../image-presentation.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { ImagePresentationUpdateComponent } from './image-presentation-update.component';

describe('ImagePresentation Management Update Component', () => {
  let comp: ImagePresentationUpdateComponent;
  let fixture: ComponentFixture<ImagePresentationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imagePresentationFormService: ImagePresentationFormService;
  let imagePresentationService: ImagePresentationService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ImagePresentationUpdateComponent],
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
      .overrideTemplate(ImagePresentationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagePresentationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imagePresentationFormService = TestBed.inject(ImagePresentationFormService);
    imagePresentationService = TestBed.inject(ImagePresentationService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Product query and add missing value', () => {
      const imagePresentation: IImagePresentation = { id: 456 };
      const imagesPresentation: IProduct = { id: 59335 };
      imagePresentation.imagesPresentation = imagesPresentation;

      const productCollection: IProduct[] = [{ id: 58793 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const additionalProducts = [imagesPresentation];
      const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ imagePresentation });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(
        productCollection,
        ...additionalProducts.map(expect.objectContaining)
      );
      expect(comp.productsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const imagePresentation: IImagePresentation = { id: 456 };
      const imagesPresentation: IProduct = { id: 73813 };
      imagePresentation.imagesPresentation = imagesPresentation;

      activatedRoute.data = of({ imagePresentation });
      comp.ngOnInit();

      expect(comp.productsSharedCollection).toContain(imagesPresentation);
      expect(comp.imagePresentation).toEqual(imagePresentation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePresentation>>();
      const imagePresentation = { id: 123 };
      jest.spyOn(imagePresentationFormService, 'getImagePresentation').mockReturnValue(imagePresentation);
      jest.spyOn(imagePresentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePresentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePresentation }));
      saveSubject.complete();

      // THEN
      expect(imagePresentationFormService.getImagePresentation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imagePresentationService.update).toHaveBeenCalledWith(expect.objectContaining(imagePresentation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePresentation>>();
      const imagePresentation = { id: 123 };
      jest.spyOn(imagePresentationFormService, 'getImagePresentation').mockReturnValue({ id: null });
      jest.spyOn(imagePresentationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePresentation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: imagePresentation }));
      saveSubject.complete();

      // THEN
      expect(imagePresentationFormService.getImagePresentation).toHaveBeenCalled();
      expect(imagePresentationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImagePresentation>>();
      const imagePresentation = { id: 123 };
      jest.spyOn(imagePresentationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ imagePresentation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imagePresentationService.update).toHaveBeenCalled();
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
