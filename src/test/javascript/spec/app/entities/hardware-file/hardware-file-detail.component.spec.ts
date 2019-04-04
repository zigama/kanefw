/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { KanefwTestModule } from '../../../test.module';
import { HardwareFileDetailComponent } from 'app/entities/hardware-file/hardware-file-detail.component';
import { HardwareFile } from 'app/shared/model/hardware-file.model';

describe('Component Tests', () => {
    describe('HardwareFile Management Detail Component', () => {
        let comp: HardwareFileDetailComponent;
        let fixture: ComponentFixture<HardwareFileDetailComponent>;
        const route = ({ data: of({ hardwareFile: new HardwareFile(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareFileDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HardwareFileDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HardwareFileDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.hardwareFile).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
