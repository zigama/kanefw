/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KanefwTestModule } from '../../../test.module';
import { HardwareFileComponent } from 'app/entities/hardware-file/hardware-file.component';
import { HardwareFileService } from 'app/entities/hardware-file/hardware-file.service';
import { HardwareFile } from 'app/shared/model/hardware-file.model';

describe('Component Tests', () => {
    describe('HardwareFile Management Component', () => {
        let comp: HardwareFileComponent;
        let fixture: ComponentFixture<HardwareFileComponent>;
        let service: HardwareFileService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareFileComponent],
                providers: []
            })
                .overrideTemplate(HardwareFileComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HardwareFileComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareFileService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new HardwareFile(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.hardwareFiles[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
