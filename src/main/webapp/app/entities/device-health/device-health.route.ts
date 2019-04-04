import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DeviceHealth } from 'app/shared/model/device-health.model';
import { DeviceHealthService } from './device-health.service';
import { DeviceHealthComponent } from './device-health.component';
import { DeviceHealthDetailComponent } from './device-health-detail.component';
import { DeviceHealthUpdateComponent } from './device-health-update.component';
import { DeviceHealthDeletePopupComponent } from './device-health-delete-dialog.component';
import { IDeviceHealth } from 'app/shared/model/device-health.model';

@Injectable({ providedIn: 'root' })
export class DeviceHealthResolve implements Resolve<IDeviceHealth> {
    constructor(private service: DeviceHealthService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDeviceHealth> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<DeviceHealth>) => response.ok),
                map((deviceHealth: HttpResponse<DeviceHealth>) => deviceHealth.body)
            );
        }
        return of(new DeviceHealth());
    }
}

export const deviceHealthRoute: Routes = [
    {
        path: '',
        component: DeviceHealthComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.deviceHealth.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: DeviceHealthDetailComponent,
        resolve: {
            deviceHealth: DeviceHealthResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.deviceHealth.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: DeviceHealthUpdateComponent,
        resolve: {
            deviceHealth: DeviceHealthResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.deviceHealth.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: DeviceHealthUpdateComponent,
        resolve: {
            deviceHealth: DeviceHealthResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.deviceHealth.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const deviceHealthPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: DeviceHealthDeletePopupComponent,
        resolve: {
            deviceHealth: DeviceHealthResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.deviceHealth.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
