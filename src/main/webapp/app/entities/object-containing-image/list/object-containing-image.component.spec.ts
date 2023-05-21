import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ObjectContainingImageService } from '../service/object-containing-image.service';

import { ObjectContainingImageComponent } from './object-containing-image.component';

describe('ObjectContainingImage Management Component', () => {
  let comp: ObjectContainingImageComponent;
  let fixture: ComponentFixture<ObjectContainingImageComponent>;
  let service: ObjectContainingImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'object-containing-image', component: ObjectContainingImageComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [ObjectContainingImageComponent],
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
      .overrideTemplate(ObjectContainingImageComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ObjectContainingImageComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ObjectContainingImageService);

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
    expect(comp.objectContainingImages?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to objectContainingImageService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getObjectContainingImageIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getObjectContainingImageIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
