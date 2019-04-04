/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { KanefwTestModule } from '../../../test.module';
import { HardwareFileDeleteDialogComponent } from 'app/entities/hardware-file/hardware-file-delete-dialog.component';
import { HardwareFileService } from 'app/entities/hardware-file/hardware-file.service';

describe('Component Tests', () => {
    describe('HardwareFile Management Delete Component', () => {
        let comp: HardwareFileDeleteDialogComponent;
        let fixture: ComponentFixture<HardwareFileDeleteDialogComponent>;
        let service: HardwareFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KanefwTestModule],
                declarations: [HardwareFileDeleteDialogComponent]
            })
                .overrideTemplate(HardwareFileDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HardwareFileDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HardwareFileService);
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
