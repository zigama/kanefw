/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { HardwareDetailComponent } from 'app/entities/hardware/hardware-detail.component';
import { Hardware } from 'app/shared/model/hardware.model';

describe('Component Tests', () => {
    describe('Hardware Management Detail Component', () => {
        let comp: HardwareDetailComponent;
        let fixture: ComponentFixture<HardwareDetailComponent>;
        const route = ({ data: of({ hardware: new Hardware(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HardwareDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HardwareDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.hardware).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
