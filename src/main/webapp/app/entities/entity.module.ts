import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'hardware',
                loadChildren: './hardware/hardware.module#KanefwHardwareModule'
            },
            {
                path: 'hardware-file',
                loadChildren: './hardware-file/hardware-file.module#KanefwHardwareFileModule'
            },
            {
                path: 'content',
                loadChildren: './content/content.module#KanefwContentModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#KanefwDeviceModule'
            },
            {
                path: 'device-health',
                loadChildren: './device-health/device-health.module#KanefwDeviceHealthModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#KanefwCustomerModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#KanefwTransactionModule'
            },
            {
                path: 'hardware',
                loadChildren: './hardware/hardware.module#KanefwHardwareModule'
            },
            {
                path: 'hardware-file',
                loadChildren: './hardware-file/hardware-file.module#KanefwHardwareFileModule'
            },
            {
                path: 'content',
                loadChildren: './content/content.module#KanefwContentModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#KanefwDeviceModule'
            },
            {
                path: 'device-health',
                loadChildren: './device-health/device-health.module#KanefwDeviceHealthModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#KanefwCustomerModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#KanefwTransactionModule'
            },
            {
                path: 'hardware',
                loadChildren: './hardware/hardware.module#KanefwHardwareModule'
            },
            {
                path: 'hardware-file',
                loadChildren: './hardware-file/hardware-file.module#KanefwHardwareFileModule'
            },
            {
                path: 'content',
                loadChildren: './content/content.module#KanefwContentModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#KanefwDeviceModule'
            },
            {
                path: 'device-health',
                loadChildren: './device-health/device-health.module#KanefwDeviceHealthModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#KanefwCustomerModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#KanefwTransactionModule'
            },
            {
                path: 'hardware',
                loadChildren: './hardware/hardware.module#KanefwHardwareModule'
            },
            {
                path: 'hardware-file',
                loadChildren: './hardware-file/hardware-file.module#KanefwHardwareFileModule'
            },
            {
                path: 'content',
                loadChildren: './content/content.module#KanefwContentModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#KanefwDeviceModule'
            },
            {
                path: 'device-health',
                loadChildren: './device-health/device-health.module#KanefwDeviceHealthModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#KanefwCustomerModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#KanefwTransactionModule'
            },
            {
                path: 'hardware',
                loadChildren: './hardware/hardware.module#KanefwHardwareModule'
            },
            {
                path: 'hardware-file',
                loadChildren: './hardware-file/hardware-file.module#KanefwHardwareFileModule'
            },
            {
                path: 'content',
                loadChildren: './content/content.module#KanefwContentModule'
            },
            {
                path: 'device',
                loadChildren: './device/device.module#KanefwDeviceModule'
            },
            {
                path: 'device-health',
                loadChildren: './device-health/device-health.module#KanefwDeviceHealthModule'
            },
            {
                path: 'customer',
                loadChildren: './customer/customer.module#KanefwCustomerModule'
            },
            {
                path: 'transaction',
                loadChildren: './transaction/transaction.module#KanefwTransactionModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KanefwEntityModule {}
