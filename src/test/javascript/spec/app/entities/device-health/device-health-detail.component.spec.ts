/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { DeviceHealthDetailComponent } from 'app/entities/device-health/device-health-detail.component';
import { DeviceHealth } from 'app/shared/model/device-health.model';

describe('Component Tests', () => {
    describe('DeviceHealth Management Detail Component', () => {
        let comp: DeviceHealthDetailComponent;
        let fixture: ComponentFixture<DeviceHealthDetailComponent>;
        const route = ({ data: of({ deviceHealth: new DeviceHealth(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [DeviceHealthDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DeviceHealthDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DeviceHealthDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.deviceHealth).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
