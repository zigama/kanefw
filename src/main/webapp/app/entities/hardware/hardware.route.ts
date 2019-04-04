import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Hardware } from 'app/shared/model/hardware.model';
import { HardwareService } from './hardware.service';
import { HardwareComponent } from './hardware.component';
import { HardwareDetailComponent } from './hardware-detail.component';
import { HardwareUpdateComponent } from './hardware-update.component';
import { HardwareDeletePopupComponent } from './hardware-delete-dialog.component';
import { IHardware } from 'app/shared/model/hardware.model';

@Injectable({ providedIn: 'root' })
export class HardwareResolve implements Resolve<IHardware> {
    constructor(private service: HardwareService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHardware> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Hardware>) => response.ok),
                map((hardware: HttpResponse<Hardware>) => hardware.body)
            );
        }
        return of(new Hardware());
    }
}

export const hardwareRoute: Routes = [
    {
        path: '',
        component: HardwareComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardware.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: HardwareDetailComponent,
        resolve: {
            hardware: HardwareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardware.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: HardwareUpdateComponent,
        resolve: {
            hardware: HardwareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardware.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: HardwareUpdateComponent,
        resolve: {
            hardware: HardwareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardware.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hardwarePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: HardwareDeletePopupComponent,
        resolve: {
            hardware: HardwareResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardware.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
