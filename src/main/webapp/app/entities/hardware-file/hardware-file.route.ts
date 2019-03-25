import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HardwareFile } from 'app/shared/model/hardware-file.model';
import { HardwareFileService } from './hardware-file.service';
import { HardwareFileComponent } from './hardware-file.component';
import { HardwareFileDetailComponent } from './hardware-file-detail.component';
import { HardwareFileUpdateComponent } from './hardware-file-update.component';
import { HardwareFileDeletePopupComponent } from './hardware-file-delete-dialog.component';
import { IHardwareFile } from 'app/shared/model/hardware-file.model';

@Injectable({ providedIn: 'root' })
export class HardwareFileResolve implements Resolve<IHardwareFile> {
    constructor(private service: HardwareFileService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHardwareFile> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HardwareFile>) => response.ok),
                map((hardwareFile: HttpResponse<HardwareFile>) => hardwareFile.body)
            );
        }
        return of(new HardwareFile());
    }
}

export const hardwareFileRoute: Routes = [
    {
        path: '',
        component: HardwareFileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardwareFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: HardwareFileDetailComponent,
        resolve: {
            hardwareFile: HardwareFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardwareFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: HardwareFileUpdateComponent,
        resolve: {
            hardwareFile: HardwareFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardwareFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: HardwareFileUpdateComponent,
        resolve: {
            hardwareFile: HardwareFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardwareFile.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hardwareFilePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: HardwareFileDeletePopupComponent,
        resolve: {
            hardwareFile: HardwareFileResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kanefwApp.hardwareFile.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
