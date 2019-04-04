import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { KanefwSharedModule } from 'app/shared';
import {
    HardwareComponent,
    HardwareDetailComponent,
    HardwareUpdateComponent,
    HardwareDeletePopupComponent,
    HardwareDeleteDialogComponent,
    hardwareRoute,
    hardwarePopupRoute
} from './';

const ENTITY_STATES = [...hardwareRoute, ...hardwarePopupRoute];

@NgModule({
    imports: [KanefwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HardwareComponent,
        HardwareDetailComponent,
        HardwareUpdateComponent,
        HardwareDeleteDialogComponent,
        HardwareDeletePopupComponent
    ],
    entryComponents: [HardwareComponent, HardwareUpdateComponent, HardwareDeleteDialogComponent, HardwareDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KanefwHardwareModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
