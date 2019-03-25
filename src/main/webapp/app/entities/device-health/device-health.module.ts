import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { KanefwSharedModule } from 'app/shared';
import {
    DeviceHealthComponent,
    DeviceHealthDetailComponent,
    DeviceHealthUpdateComponent,
    DeviceHealthDeletePopupComponent,
    DeviceHealthDeleteDialogComponent,
    deviceHealthRoute,
    deviceHealthPopupRoute
} from './';

const ENTITY_STATES = [...deviceHealthRoute, ...deviceHealthPopupRoute];

@NgModule({
    imports: [KanefwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DeviceHealthComponent,
        DeviceHealthDetailComponent,
        DeviceHealthUpdateComponent,
        DeviceHealthDeleteDialogComponent,
        DeviceHealthDeletePopupComponent
    ],
    entryComponents: [
        DeviceHealthComponent,
        DeviceHealthUpdateComponent,
        DeviceHealthDeleteDialogComponent,
        DeviceHealthDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KanefwDeviceHealthModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
