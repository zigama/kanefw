import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { KanefwSharedModule } from 'app/shared';
import {
    HardwareFileComponent,
    HardwareFileDetailComponent,
    HardwareFileUpdateComponent,
    HardwareFileDeletePopupComponent,
    HardwareFileDeleteDialogComponent,
    hardwareFileRoute,
    hardwareFilePopupRoute
} from './';

const ENTITY_STATES = [...hardwareFileRoute, ...hardwareFilePopupRoute];

@NgModule({
    imports: [KanefwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HardwareFileComponent,
        HardwareFileDetailComponent,
        HardwareFileUpdateComponent,
        HardwareFileDeleteDialogComponent,
        HardwareFileDeletePopupComponent
    ],
    entryComponents: [
        HardwareFileComponent,
        HardwareFileUpdateComponent,
        HardwareFileDeleteDialogComponent,
        HardwareFileDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KanefwHardwareFileModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
