/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KanefwTestModule } from '../../../test.module';
import { HardwareDeleteDialogComponent } from 'app/entities/hardware/hardware-delete-dialog.component';
import { HardwareService } from 'app/entities/hardware/hardware.service';

describe('Component Tests', () => {
    describe('Hardware Management Delete Component', () => {
        let comp: HardwareDeleteDialogComponent;
        let fixture: ComponentFixture<HardwareDeleteDialogComponent>;
        let service: HardwareService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareDeleteDialogComponent]
            })
                .overrideTemplate(HardwareDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HardwareDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
