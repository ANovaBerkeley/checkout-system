User.delete_all
User.create([{ name: 'ANova', email: 'demo@demo.com', password: "change_me", remember_created_at: nil }])
