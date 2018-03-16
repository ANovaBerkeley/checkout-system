class CreateMentors < ActiveRecord::Migration[5.0]
  def change
    create_table :mentors do |t|
      t.string :name
      t.string :email
      t.string :phone
      t.string :upc

      t.timestamps
    end
  end
end
