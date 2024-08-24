//
//  ContentView.swift
//  iosApp
//
//  Created by Mac on 17/08/24.
//

import SwiftUI
import shared
import UIKit

struct ComposeView: UIViewControllerRepresentable{
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.keyboard)
    }
}


#Preview {
    ContentView()
}
