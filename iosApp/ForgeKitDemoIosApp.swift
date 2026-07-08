import ForgeKitDemo
import SwiftUI
import UIKit

@main
struct ForgeKitDemoIosApp: App {
    var body: some Scene {
        WindowGroup {
            ForgeKitDemoView()
                .ignoresSafeArea()
        }
    }
}

struct ForgeKitDemoView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
