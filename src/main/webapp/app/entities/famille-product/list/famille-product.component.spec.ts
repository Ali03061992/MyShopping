import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FamilleProductService } from '../service/famille-product.service';

import { FamilleProductComponent } from './famille-product.component';

describe('FamilleProduct Management Component', () => {
  let comp: FamilleProductComponent;
  let fixture: ComponentFixture<FamilleProductComponent>;
  let service: FamilleProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'famille-product', component: FamilleProductComponent }]), HttpClientTestingModule],
      declarations: [FamilleProductComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FamilleProductComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleProductComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FamilleProductService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.familleProducts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to familleProductService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFamilleProductIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFamilleProductIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
