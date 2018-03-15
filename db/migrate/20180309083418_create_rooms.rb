class CreateRooms < ActiveRecord::Migration[5.0]
  def change
    create_table :rooms do |t|
      t.string :name
      t.date :date
      t.time :time
      t.string :location
      t.string :upc

      t.timestamps
    end
  end
end
